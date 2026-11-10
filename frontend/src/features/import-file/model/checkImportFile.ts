import type { z } from 'zod'
import { IMPORT_RESOURCE_TYPES, type ImportResourceType } from '@/entities/import-operation'
import { organizationImportItemSchema } from '@/entities/organization'
import { personImportItemSchema } from '@/entities/person'
import { productImportItemSchema } from '@/entities/product'
import { describeZodIssue } from './describeZodIssue'

export const MAX_FILE_SIZE_BYTES = 2 * 1024 * 1024
export const MAX_ITEMS = 1024

const ITEM_SCHEMAS = {
  products: productImportItemSchema,
  organizations: organizationImportItemSchema,
  persons: personImportItemSchema,
} as const satisfies Record<ImportResourceType, z.ZodType>

const RESOURCE_LABELS: Record<ImportResourceType, string> = {
  products: 'продукты',
  organizations: 'организации',
  persons: 'персоны',
}

const isResourceType = (value: unknown): value is ImportResourceType =>
  IMPORT_RESOURCE_TYPES.some((type) => type === value)

export async function checkImportFile(file: File, expected: ImportResourceType): Promise<string | null> {
  if (!file.name.toLowerCase().endsWith('.json')) return 'Допускаются только файлы с расширением .json'
  if (file.size === 0) return 'Файл пуст'
  if (file.size > MAX_FILE_SIZE_BYTES) return 'Размер файла превышает 2 МБ'

  let data: unknown
  try {
    data = JSON.parse((await file.text()).replace(/^﻿/, ''))
  } catch {
    return 'Файл не является корректным JSON'
  }

  if (typeof data !== 'object' || data === null || Array.isArray(data)) {
    return 'Ожидался JSON-объект с ключами resourceType и items'
  }

  const extraKeys = Object.keys(data).filter((key) => key !== 'resourceType' && key !== 'items')
  if (extraKeys.length > 0) return `Лишние ключи верхнего уровня: ${extraKeys.join(', ')}`

  const { resourceType, items } = data as { resourceType?: unknown; items?: unknown }

  if (resourceType === undefined) return 'Не указан ключ resourceType'
  if (!isResourceType(resourceType)) {
    return `Неизвестный resourceType. Допустимые значения: ${IMPORT_RESOURCE_TYPES.join(', ')}`
  }
  if (resourceType !== expected) {
    return `Файл предназначен для другого типа данных: ${RESOURCE_LABELS[resourceType]}. Здесь можно загрузить только ${RESOURCE_LABELS[expected]}`
  }

  if (!Array.isArray(items)) return 'Ключ items должен быть массивом записей'
  if (items.length === 0) return 'В файле нет записей: массив items пуст'
  if (items.length > MAX_ITEMS) return `Допускается не более ${MAX_ITEMS} записей, в файле их ${items.length}`

  const schema = ITEM_SCHEMAS[expected]
  for (let position = 0; position < items.length; position++) {
    const result = schema.safeParse(items[position])
    if (!result.success) return describeZodIssue(result.error.issues[0], items[position], position + 1)
  }

  return null
}

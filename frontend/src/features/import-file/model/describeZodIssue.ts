import type { z } from 'zod'

const EXPECTED: Record<string, string> = {
  string: 'ожидается строка',
  number: 'ожидается число',
  int: 'ожидается целое число',
  object: 'ожидается объект',
  array: 'ожидается массив',
  boolean: 'ожидается логическое значение',
}

function valueAt(record: unknown, path: PropertyKey[]): unknown {
  return path.reduce<unknown>((current, key) => (current as Record<PropertyKey, unknown> | null | undefined)?.[key], record)
}

const toPath = (path: PropertyKey[]) => path.map(String).join('.')

const format = (index: number, path: string, message: string) =>
  `Запись ${index}${path ? `, поле ${path}` : ''}: ${message}`

export function describeZodIssue(issue: z.core.$ZodIssue, record: unknown, index: number): string {
  if (issue.code === 'unrecognized_keys') {
    return format(index, toPath([...issue.path, issue.keys[0]]), 'неизвестное поле')
  }

  const path = toPath(issue.path)
  const value = valueAt(record, issue.path)
  const missing = value === undefined || value === null

  if (issue.code === 'invalid_type') {
    if (missing) return format(index, path, 'обязательное поле')
    const expected = EXPECTED[issue.expected]
    return format(index, path, expected ? `неверный тип значения, ${expected}` : 'неверный тип значения')
  }

  if (issue.code === 'invalid_value') {
    return format(index, path, missing ? 'обязательное поле' : 'недопустимое значение')
  }

  return format(index, path, 'некорректное значение')
}

import type { Person, PersonFilters } from '@/entities/person'
import type { DataTableColumn } from '@/shared/ui'

export type FilterKey = keyof PersonFilters

const dash = '—'

export const columns: DataTableColumn<Person>[] = [
  { id: 'id', title: 'ID', sortField: 'id', render: (p) => p.id },
  {
    id: 'name',
    title: 'Имя',
    sortField: 'name',
    filter: { key: 'name' satisfies FilterKey, kind: 'text' },
    render: (p) => p.name,
  },
  {
    id: 'eyeColor',
    title: 'Цвет глаз',
    sortField: 'eyeColor',
    render: (p) => p.eyeColor ?? dash,
  },
  {
    id: 'hairColor',
    title: 'Цвет волос',
    sortField: 'hairColor',
    render: (p) => p.hairColor,
  },
  { id: 'weight', title: 'Вес', sortField: 'weight', render: (p) => p.weight ?? dash },
  {
    id: 'nationality',
    title: 'Национальность',
    sortField: 'nationality',
    render: (p) => p.nationality,
  },
]

export const SORT_FIELDS = new Set(columns.flatMap((column) => (column.sortField ? [column.sortField] : [])))

export const FILTER_KEYS: FilterKey[] = columns.flatMap((column) =>
  column.filter ? [column.filter.key as FilterKey] : [],
)

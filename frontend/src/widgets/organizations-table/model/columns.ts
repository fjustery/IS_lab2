import type { Organization, OrganizationFilters } from '@/entities/organization'
import type { DataTableColumn } from '@/shared/ui'

export type FilterKey = keyof OrganizationFilters

const dash = '—'

export const columns: DataTableColumn<Organization>[] = [
  { id: 'id', title: 'ID', sortField: 'id', render: (o) => o.id },
  {
    id: 'name',
    title: 'Название',
    sortField: 'name',
    filter: { key: 'name' satisfies FilterKey, kind: 'text' },
    render: (o) => o.name,
  },
  {
    id: 'type',
    title: 'Тип',
    sortField: 'type',
    render: (o) => o.type ?? dash,
  },
  {
    id: 'annualTurnover',
    title: 'Годовой оборот',
    sortField: 'annualTurnover',
    render: (o) => o.annualTurnover,
  },
  { id: 'employeesCount', title: 'Сотрудников', sortField: 'employeesCount', render: (o) => o.employeesCount },
  { id: 'rating', title: 'Рейтинг', sortField: 'rating', render: (o) => o.rating },
  {
    id: 'officialZip',
    title: 'Индекс (офиц.)',
    sortField: 'officialAddress.zipCode',
    render: (o) => o.officialAddress.zipCode ?? dash,
  },
  {
    id: 'postalZip',
    title: 'Индекс (почт.)',
    sortField: 'postalAddress.zipCode',
    render: (o) => o.postalAddress.zipCode ?? dash,
  },
]

export const SORT_FIELDS = new Set(columns.flatMap((column) => (column.sortField ? [column.sortField] : [])))

export const FILTER_KEYS: FilterKey[] = columns.flatMap((column) =>
  column.filter ? [column.filter.key as FilterKey] : [],
)

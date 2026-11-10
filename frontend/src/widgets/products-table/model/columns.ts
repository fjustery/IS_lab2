import type { Product, ProductFilters } from '@/entities/product'
import type { DataTableColumn } from '@/shared/ui'

export type FilterKey = keyof ProductFilters

const dash = '—'

export const columns: DataTableColumn<Product>[] = [
  { id: 'id', title: 'ID', sortField: 'id', render: (p) => p.id },
  {
    id: 'name',
    title: 'Название',
    sortField: 'name',
    filter: { key: 'name' satisfies FilterKey, kind: 'text' },
    render: (p) => p.name,
  },
  { id: 'x', title: 'Координата X', sortField: 'coordinates.x', render: (p) => p.coordinates.x },
  { id: 'y', title: 'Координата Y', sortField: 'coordinates.y', render: (p) => p.coordinates.y },
  {
    id: 'creationDate',
    title: 'Дата создания',
    sortField: 'creationDate',
    render: (p) => new Date(p.creationDate).toLocaleString('ru-RU'),
  },
  {
    id: 'unitOfMeasure',
    title: 'Ед. измерения',
    sortField: 'unitOfMeasure',
    render: (p) => p.unitOfMeasure,
  },
  {
    id: 'manufacturer',
    title: 'Производитель',
    sortField: 'manufacturer.name',
    filter: { key: 'manufacturerName' satisfies FilterKey, kind: 'text' },
    render: (p) => p.manufacturer.name,
  },
  { id: 'price', title: 'Цена', sortField: 'price', render: (p) => p.price },
  {
    id: 'manufactureCost',
    title: 'Себестоимость',
    sortField: 'manufactureCost',
    render: (p) => p.manufactureCost ?? dash,
  },
  { id: 'rating', title: 'Рейтинг', sortField: 'rating', render: (p) => p.rating },
  {
    id: 'partNumber',
    title: 'Партийный номер',
    sortField: 'partNumber',
    filter: { key: 'partNumber' satisfies FilterKey, kind: 'text' },
    render: (p) => p.partNumber,
  },
  {
    id: 'owner',
    title: 'Владелец',
    sortField: 'owner.name',
    filter: { key: 'ownerName' satisfies FilterKey, kind: 'text' },
    render: (p) => p.owner.name,
  },
]

export const SORT_FIELDS = new Set(columns.flatMap((column) => (column.sortField ? [column.sortField] : [])))

export const FILTER_KEYS: FilterKey[] = columns.flatMap((column) =>
  column.filter ? [column.filter.key as FilterKey] : [],
)

import type { ReactNode } from 'react'
import { Link, useLocation } from 'react-router'
import { Button } from '../Button'
import { FilterCell, type FilterKind } from '../FilterCell'
import { Pagination } from '../Pagination'
import styles from './DataTable.module.css'

export interface DataTableColumn<T> {
  id: string
  title: string
  sortField?: string
  filter?: { key: string } & FilterKind
  render: (row: T) => ReactNode
}

interface Props<T> {
  columns: DataTableColumn<T>[]
  rows: T[] | undefined
  isFetching: boolean
  isError: boolean
  onRetry: () => void
  totalElements: number | undefined
  totalPages: number | undefined
  page: number
  onPageChange: (page: number) => void
  sort?: { field: string; direction: 'asc' | 'desc' }
  onToggleSort?: (field: string) => void
  filters?: Record<string, string | undefined>
  onFilterChange?: (key: string, value: string) => void
  hasFilters?: boolean
  onResetFilters?: () => void
  getRowId: (row: T) => number | string
  getRowHref?: (row: T) => string
  getRowAriaLabel?: (row: T) => string
  emptyMessage: string
}

const dash = '—'

export function DataTable<T>({
  columns,
  rows,
  isFetching,
  isError,
  onRetry,
  totalElements,
  totalPages,
  page,
  onPageChange,
  sort,
  onToggleSort,
  filters,
  onFilterChange,
  hasFilters,
  onResetFilters,
  getRowId,
  getRowHref,
  getRowAriaLabel,
  emptyMessage,
}: Props<T>) {
  const { search } = useLocation()

  if (isError) {
    return (
      <div className={styles.state}>
        Не удалось загрузить список.{' '}
        <Button variant="secondary" size="sm" onClick={onRetry}>
          Повторить
        </Button>
      </div>
    )
  }

  const [firstColumn, ...restColumns] = columns
  const hasFilterableColumns = columns.some((column) => column.filter)

  return (
    <div className={styles.root}>
      {hasFilters && (
        <div className={styles.toolbar}>
          <Button variant="secondary" onClick={onResetFilters}>
            Сбросить фильтры
          </Button>
        </div>
      )}

      <div className={styles.scroll}>
        <table className={styles.table}>
          <thead>
            <tr>
              {columns.map((column) => {
                const { sortField } = column
                const active = sortField && sort?.field === sortField ? sort.direction : undefined
                return (
                  <th
                    key={column.id}
                    aria-sort={
                      active === 'asc' ? 'ascending' : active === 'desc' ? 'descending' : undefined
                    }
                  >
                    {onToggleSort && sortField ? (
                      <Button
                        variant="ghost"
                        className={styles.sortButton}
                        onClick={() => onToggleSort(sortField)}
                      >
                        {column.title}
                        <span className={styles.arrow} aria-hidden>
                          {active === 'asc' ? '▲' : active === 'desc' ? '▼' : '⇅'}
                        </span>
                      </Button>
                    ) : (
                      column.title
                    )}
                  </th>
                )
              })}
            </tr>
            {hasFilterableColumns && (
              <tr className={styles.filterRow}>
                {columns.map((column) => (
                  <th key={column.id}>
                    {column.filter && (
                      <FilterCell
                        filter={column.filter}
                        title={column.title}
                        value={filters?.[column.filter.key] ?? ''}
                        onChange={(value) => onFilterChange?.(column.filter!.key, value)}
                      />
                    )}
                  </th>
                ))}
              </tr>
            )}
          </thead>
          <tbody className={isFetching ? styles.loading : undefined}>
            {rows?.map((row) => (
              <tr key={getRowId(row)}>
                <td>
                  {getRowHref && getRowAriaLabel ? (
                    <Link
                      className={styles.stretchedLink}
                      to={{ pathname: getRowHref(row), search }}
                      aria-label={getRowAriaLabel(row)}
                    >
                      {firstColumn.render(row)}
                    </Link>
                  ) : (
                    firstColumn.render(row)
                  )}
                </td>
                {restColumns.map((column) => (
                  <td key={column.id}>{column.render(row)}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
        {rows && rows.length === 0 && (
          <div className={styles.state}>{hasFilters ? 'Ничего не найдено' : emptyMessage}</div>
        )}
        {!rows && isFetching && <div className={styles.state}>Загрузка…</div>}
      </div>

      <div className={styles.footer}>
        <span className={styles.total}>Всего: {totalElements ?? dash}</span>
        <Pagination page={page} totalPages={totalPages ?? 1} onChange={onPageChange} />
      </div>
    </div>
  )
}

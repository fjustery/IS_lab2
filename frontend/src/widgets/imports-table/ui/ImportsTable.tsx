import { useGetImportsQuery } from '@/entities/import-operation'
import { PAGE_SIZE, useTableQueryState } from '@/shared/lib'
import { DataTable } from '@/shared/ui'
import { columns } from '../model/columns'

const NO_SORT_FIELDS: ReadonlySet<string> = new Set()
const NO_FILTER_KEYS: readonly never[] = []

export function ImportsTable() {
  const { page, setPage } = useTableQueryState<Record<never, string>>(NO_SORT_FIELDS, NO_FILTER_KEYS)

  const { data, isFetching, isError, refetch } = useGetImportsQuery({ page, size: PAGE_SIZE })

  return (
    <DataTable
      columns={columns}
      rows={data?.content}
      isFetching={isFetching}
      isError={isError}
      onRetry={refetch}
      totalElements={data?.totalElements}
      totalPages={data?.totalPages}
      page={page}
      onPageChange={setPage}
      getRowId={(operation) => operation.id}
      emptyMessage="Операций импорта пока не было"
    />
  )
}

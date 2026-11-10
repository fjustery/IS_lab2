import { Outlet } from 'react-router'
import { routes } from '@/shared/config'
import { PageHeader } from '@/shared/ui'
import { PersonsTable } from '@/widgets/persons-table'

export function PersonsPage() {
  return (
    <>
      <PageHeader
        title="Персоны"
        createLabel="Создать"
        createTo={routes.personNew}
        importLabel="Импортировать"
        importTo={routes.personImport}
      />
      <PersonsTable />
      <Outlet />
    </>
  )
}

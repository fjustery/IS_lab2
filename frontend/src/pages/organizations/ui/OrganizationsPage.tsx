import { Outlet } from 'react-router'
import { routes } from '@/shared/config'
import { PageHeader } from '@/shared/ui'
import { OrganizationsTable } from '@/widgets/organizations-table'

export function OrganizationsPage() {
  return (
    <>
      <PageHeader
        title="Организации"
        createLabel="Создать"
        createTo={routes.organizationNew}
        importLabel="Импортировать"
        importTo={routes.organizationImport}
      />
      <OrganizationsTable />
      <Outlet />
    </>
  )
}

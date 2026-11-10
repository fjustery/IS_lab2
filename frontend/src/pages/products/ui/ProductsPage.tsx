import { Outlet } from 'react-router'
import { routes } from '@/shared/config'
import { PageHeader } from '@/shared/ui'
import { ProductsTable } from '@/widgets/products-table'

export function ProductsPage() {
  return (
    <>
      <PageHeader
        title="Продукция"
        createLabel="Создать"
        createTo={routes.productNew}
        importLabel="Импортировать"
        importTo={routes.productImport}
      />
      <ProductsTable />
      <Outlet />
    </>
  )
}

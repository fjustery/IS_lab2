import { createHashRouter, Navigate } from 'react-router'
import { ImportFilePage } from '@/pages/import-file'
import { ImportsPage } from '@/pages/imports'
import { LoginPage } from '@/pages/login'
import { OrganizationCreatePage } from '@/pages/organization-create'
import { OrganizationEditPage } from '@/pages/organization-edit'
import { OrganizationViewPage } from '@/pages/organization-view'
import { OrganizationsPage } from '@/pages/organizations'
import { PersonCreatePage } from '@/pages/person-create'
import { PersonEditPage } from '@/pages/person-edit'
import { PersonViewPage } from '@/pages/person-view'
import { PersonsPage } from '@/pages/persons'
import { ProductCreatePage } from '@/pages/product-create'
import { ProductEditPage } from '@/pages/product-edit'
import { ProductViewPage } from '@/pages/product-view'
import { ProductsPage } from '@/pages/products'
import { RegisterPage } from '@/pages/register'
import { SpecialOperationModalPage } from '@/pages/special-operation'
import { SpecialOperationsPage } from '@/pages/special-operations'
import { AppLayout } from './AppLayout'
import { GuestOnlyRoute, ProtectedRoute } from './guards'

export const router = createHashRouter([
  { path: 'login', element: <GuestOnlyRoute><LoginPage /></GuestOnlyRoute> },
  { path: 'register', element: <GuestOnlyRoute><RegisterPage /></GuestOnlyRoute> },
  {
    path: '/',
    element: (
      <ProtectedRoute>
        <AppLayout />
      </ProtectedRoute>
    ),
    children: [
      { index: true, element: <Navigate to="/products" replace /> },
      {
        path: 'products',
        element: <ProductsPage />,
        children: [
          { path: 'new', element: <ProductCreatePage /> },
          { path: 'import', element: <ImportFilePage resourceType="products" /> },
          { path: ':id', element: <ProductViewPage /> },
          { path: ':id/edit', element: <ProductEditPage /> },
        ],
      },
      {
        path: 'organizations',
        element: <OrganizationsPage />,
        children: [
          { path: 'new', element: <OrganizationCreatePage /> },
          { path: 'import', element: <ImportFilePage resourceType="organizations" /> },
          { path: ':id', element: <OrganizationViewPage /> },
          { path: ':id/edit', element: <OrganizationEditPage /> },
        ],
      },
      {
        path: 'persons',
        element: <PersonsPage />,
        children: [
          { path: 'new', element: <PersonCreatePage /> },
          { path: 'import', element: <ImportFilePage resourceType="persons" /> },
          { path: ':id', element: <PersonViewPage /> },
          { path: ':id/edit', element: <PersonEditPage /> },
        ],
      },
      { path: 'imports', element: <ImportsPage /> },
      {
        path: 'special',
        element: <SpecialOperationsPage />,
        children: [{ path: ':operation', element: <SpecialOperationModalPage /> }],
      },
    ],
  },
])

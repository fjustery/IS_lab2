import { baseApi, type Page } from '@/shared/api'
import type { ImportOperation, ImportOperationsQuery, ImportResourceType } from '../model/types'

const IMPORTED_TAG = {
  products: 'Product',
  organizations: 'Organization',
  persons: 'Person',
} as const satisfies Record<ImportResourceType, string>

export const importApi = baseApi.injectEndpoints({
  endpoints: (build) => ({
    getImports: build.query<Page<ImportOperation>, ImportOperationsQuery>({
      query: ({ page, size }) => ({ url: '/import', params: { page, size } }),
      providesTags: [{ type: 'Import', id: 'LIST' }],
    }),
    importFile: build.mutation<ImportOperation, { resourceType: ImportResourceType; file: File }>({
      query: ({ file }) => {
        const body = new FormData()
        body.append('file', file)
        return { url: '/import', method: 'POST', body }
      },
      invalidatesTags: (result, _error, { resourceType }) =>
        result
          ? [{ type: IMPORTED_TAG[resourceType], id: 'LIST' }, { type: 'Import', id: 'LIST' }]
          : [{ type: 'Import', id: 'LIST' }],
    }),
  }),
})

export const { useGetImportsQuery, useImportFileMutation } = importApi

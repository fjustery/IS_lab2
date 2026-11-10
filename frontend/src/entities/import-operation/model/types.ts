export const IMPORT_RESOURCE_TYPES = ['products', 'organizations', 'persons'] as const
export type ImportResourceType = (typeof IMPORT_RESOURCE_TYPES)[number]

export type ImportStatus = 'SUCCESS' | 'FAILED'

export interface ImportOperation {
  id: number
  status: ImportStatus
  username: string
  addedCount: number | null
}

export interface ImportOperationsQuery {
  page: number
  size: number
}

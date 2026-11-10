import type { ImportResourceType } from '@/entities/import-operation'
import { ImportFileForm } from '@/features/import-file'
import { routes } from '@/shared/config'
import { useCloseModal } from '@/shared/lib'
import { Modal } from '@/shared/ui'

const LIST_ROUTES = {
  products: routes.products,
  organizations: routes.organizations,
  persons: routes.persons,
} as const satisfies Record<ImportResourceType, string>

interface Props {
  resourceType: ImportResourceType
}

export function ImportFilePage({ resourceType }: Props) {
  const close = useCloseModal(LIST_ROUTES[resourceType])

  return (
    <Modal title="Выберите файл для импорта" onClose={close}>
      <ImportFileForm resourceType={resourceType} onClose={close} />
    </Modal>
  )
}

import type { ImportOperation, ImportStatus } from '@/entities/import-operation'
import type { DataTableColumn } from '@/shared/ui'

const dash = '—'

const STATUS_LABELS: Record<ImportStatus, string> = {
  SUCCESS: 'Успешно',
  FAILED: 'Ошибка',
}

export const columns: DataTableColumn<ImportOperation>[] = [
  { id: 'id', title: 'ID', render: (operation) => operation.id },
  { id: 'status', title: 'Статус', render: (operation) => STATUS_LABELS[operation.status] },
  { id: 'username', title: 'Пользователь', render: (operation) => operation.username },
  {
    id: 'addedCount',
    title: 'Добавлено объектов',
    render: (operation) =>
      operation.status === 'SUCCESS' && operation.addedCount !== null ? operation.addedCount : dash,
  },
]

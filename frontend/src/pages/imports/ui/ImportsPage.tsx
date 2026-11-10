import { ImportsTable } from '@/widgets/imports-table'
import styles from './ImportsPage.module.css'

export function ImportsPage() {
  return (
    <>
      <h1 className={styles.title}>Журнал импорта</h1>
      <ImportsTable />
    </>
  )
}

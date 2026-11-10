import { useLocation } from 'react-router'
import { LinkButton } from '../LinkButton'
import styles from './PageHeader.module.css'

interface Props {
  title: string
  createLabel: string
  createTo: string
  importLabel: string
  importTo: string
}

export function PageHeader({ title, createLabel, createTo, importLabel, importTo }: Props) {
  const { search } = useLocation()

  return (
    <div className={styles.head}>
      <h1>{title}</h1>
      <div className={styles.actions}>
        <LinkButton variant="secondary" to={{ pathname: importTo, search }}>
          {importLabel}
        </LinkButton>
        <LinkButton to={{ pathname: createTo, search }}>{createLabel}</LinkButton>
      </div>
    </div>
  )
}

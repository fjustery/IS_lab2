import { useSelector } from 'react-redux'
import { NavLink } from 'react-router'
import { selectAuth } from '@/entities/user'
import { LogoutButton } from '@/features/logout'
import { routes } from '@/shared/config'
import styles from './AppHeader.module.css'

const links = [
  { to: routes.products, label: 'Продукция' },
  { to: routes.organizations, label: 'Организации' },
  { to: routes.persons, label: 'Персоны' },
  { to: routes.imports, label: 'Журнал импорта' },
  { to: routes.special, label: 'СВО' },
]

export function AppHeader() {
  const { user } = useSelector(selectAuth)

  return (
    <header className={styles.root}>
      <span className={styles.brand}>Управление продукцией</span>
      <nav className={styles.nav}>
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) => (isActive ? styles.active : undefined)}
          >
            {link.label}
          </NavLink>
        ))}
      </nav>
      <div className={styles.user}>
        <span>{user?.username}</span>
        <LogoutButton />
      </div>
    </header>
  )
}

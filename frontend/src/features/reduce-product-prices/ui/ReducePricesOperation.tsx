import { useState, type FormEvent } from 'react'
import { useReducePricesMutation } from '@/entities/product'
import { getErrorMessage } from '@/shared/api'
import { Button, FormField, Input } from '@/shared/ui'
import styles from './ReducePricesOperation.module.css'

export function ReducePricesOperation() {
  const [showResult, setShowResult] = useState(false)
  const [percent, setPercent] = useState('')
  const [reducePrices, { isLoading }] = useReducePricesMutation()
  const [message, setMessage] = useState<string>()
  const [error, setError] = useState<string>()

  const submit = async (event: FormEvent) => {
    event.preventDefault()
    const value = Number(percent)
    if (!window.confirm(`Снизить цену всей продукции на ${value}%? Действие необратимо.`)) return

    try {
      const result = await reducePrices({ percent: value }).unwrap()
      setMessage(`${result.message}`)
      setError(undefined)
    } catch (err) {
      setError(getErrorMessage(err, 'Не удалось выполнить операцию'))
      setMessage(undefined)
    }
    setShowResult(true)
  }

  const valid = Number(percent) > 0 && Number(percent) < 100

  if (!showResult) {
    return (
      <form onSubmit={submit} className={styles.form}>
        <FormField label="Процент (0–100, не включая границы)">
          <Input type="number" step="any" value={percent} onChange={(event) => setPercent(event.target.value)} />
        </FormField>
        <Button variant="danger" type="submit" disabled={!valid || isLoading}>
          {isLoading ? 'Снижаем…' : 'Выполнить'}
        </Button>
      </form>
    )
  }

  return (
    <div className={styles.result}>
      {error && <p role="alert">{error}</p>}
      {message && <p>{message}</p>}
      <Button
        variant="secondary"
        onClick={() => {
          setShowResult(false)
          setMessage(undefined)
          setError(undefined)
        }}
      >
        Назад
      </Button>
    </div>
  )
}

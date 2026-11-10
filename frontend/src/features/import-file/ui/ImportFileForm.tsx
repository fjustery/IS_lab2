import { useRef, useState, type ChangeEvent, type FormEvent } from 'react'
import { useImportFileMutation, type ImportOperation, type ImportResourceType } from '@/entities/import-operation'
import { getErrorMessage } from '@/shared/api'
import { Button, FormField, Input } from '@/shared/ui'
import { checkImportFile } from '../model/checkImportFile'
import styles from './ImportFileForm.module.css'

interface Props {
  resourceType: ImportResourceType
  onClose: () => void
}

export function ImportFileForm({ resourceType, onClose }: Props) {
  const [importFile, { isLoading }] = useImportFileMutation()
  const [file, setFile] = useState<File | null>(null)
  const [problem, setProblem] = useState<string | null>(null)
  const [result, setResult] = useState<ImportOperation | null>(null)
  const latestCheck = useRef(0)
  const inputRef = useRef<HTMLInputElement>(null)

  const onChange = async (event: ChangeEvent<HTMLInputElement>) => {
    const selected = event.target.files?.[0] ?? null
    const check = ++latestCheck.current
    setFile(null)
    setProblem(null)
    if (!selected) return

    const found = await checkImportFile(selected, resourceType)
    if (check !== latestCheck.current) return

    if (found) setProblem(found)
    else setFile(selected)
  }

  const submit = async (event: FormEvent) => {
    event.preventDefault()
    if (!file) return
    setProblem(null)
    try {
      setResult(await importFile({ resourceType, file }).unwrap())
    } catch (error) {
      setProblem(getErrorMessage(error, 'Не удалось выполнить импорт'))
    } finally {
      setFile(null)
      if (inputRef.current) inputRef.current.value = ''
    }
  }

  if (result) {
    return (
      <div className={styles.form}>
        <p className={styles.success}>Импорт выполнен. Добавлено объектов: {result.addedCount}</p>
        <div className={styles.actions}>
          <Button onClick={onClose}>Закрыть</Button>
        </div>
      </div>
    )
  }

  return (
    <form className={styles.form} onSubmit={submit} noValidate>
      <FormField label="Файл (.json)">
        <Input ref={inputRef} type="file" accept=".json,application/json" onChange={onChange} />
      </FormField>
      {problem && (
        <p className={styles.error} role="alert">
          {problem}
        </p>
      )}
      <div className={styles.actions}>
        <Button variant="secondary" onClick={onClose}>
          Отмена
        </Button>
        <Button type="submit" disabled={!file || isLoading}>
          {isLoading ? 'Импортируем…' : 'Импортировать'}
        </Button>
      </div>
    </form>
  )
}

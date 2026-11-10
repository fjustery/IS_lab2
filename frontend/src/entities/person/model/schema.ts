import { z } from 'zod'
import { checkLocationGroup, emptyLocationFormValues, locationGroupSchema } from '@/entities/location/@x/person'
import { COLORS, COUNTRIES } from './types'

const SELECT_REQUIRED = 'Выберите значение из списка'

export const personSchema = z
  .object({
    name: z.string().trim().min(1, 'Имя не может быть пустым'),
    eyeColor: z.string(),
    hairColor: z.enum(COLORS, { error: SELECT_REQUIRED }),
    location: locationGroupSchema,
    weight: z
      .number({ error: 'Введите число' })
      .int('Значение должно быть целым')
      .positive('Вес должен быть больше 0')
      .nullable(),
    nationality: z.enum(COUNTRIES, { error: SELECT_REQUIRED }),
  })
  .superRefine((person, ctx) => {
    checkLocationGroup(person.location, ctx, ['location'])
  })

export type PersonFormValues = z.infer<typeof personSchema>

export const emptyPersonFormValues: PersonFormValues = {
  name: '',
  eyeColor: '',
  hairColor: '' as unknown as PersonFormValues['hairColor'],
  location: emptyLocationFormValues,
  weight: null,
  nationality: '' as unknown as PersonFormValues['nationality'],
}

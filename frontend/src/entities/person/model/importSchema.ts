import { z } from 'zod'
import { locationImportSchema } from '@/entities/location/@x/person'
import { COLORS, COUNTRIES } from './types'

export const personImportItemSchema = z.strictObject({
  name: z.string(),
  eyeColor: z.enum(COLORS).nullish(),
  hairColor: z.enum(COLORS),
  location: locationImportSchema.nullish(),
  weight: z.number().int().nullish(),
  nationality: z.enum(COUNTRIES),
})

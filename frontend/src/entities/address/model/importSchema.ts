import { z } from 'zod'
import { locationImportSchema } from '@/entities/location/@x/address'

export const addressImportSchema = z.strictObject({
  zipCode: z.string().nullish(),
  town: locationImportSchema.nullish(),
})

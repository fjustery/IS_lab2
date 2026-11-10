import { z } from 'zod'
import { UNITS_OF_MEASURE } from './types'

export const productImportItemSchema = z.strictObject({
  name: z.string(),
  coordinates: z.strictObject({ x: z.number().int(), y: z.number() }),
  unitOfMeasure: z.enum(UNITS_OF_MEASURE),
  price: z.number(),
  manufactureCost: z.number().nullish(),
  rating: z.number(),
  partNumber: z.string(),
  manufacturerId: z.number().int(),
  ownerId: z.number().int(),
})

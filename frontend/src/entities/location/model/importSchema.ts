import { z } from 'zod'

export const locationImportSchema = z.strictObject({
  x: z.number().int(),
  y: z.number().int(),
  name: z.string().nullish(),
})

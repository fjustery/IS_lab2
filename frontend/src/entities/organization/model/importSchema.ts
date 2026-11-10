import { z } from 'zod'
import { addressImportSchema } from '@/entities/address/@x/organization'
import { ORGANIZATION_TYPES } from './types'

export const organizationImportItemSchema = z.strictObject({
  name: z.string(),
  officialAddress: addressImportSchema,
  annualTurnover: z.number().int(),
  employeesCount: z.number().int(),
  rating: z.number(),
  type: z.enum(ORGANIZATION_TYPES).nullish(),
  postalAddress: addressImportSchema,
})

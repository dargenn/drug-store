import { IDrug } from 'app/shared/model//drug.model';
import { IProducer } from 'app/shared/model//producer.model';
import { IDisease } from 'app/shared/model//disease.model';

export const enum Form {
  TABLET = 'TABLET',
  SOLUTION = 'SOLUTION',
  CAPSULE = 'CAPSULE',
  PILL = 'PILL'
}

export const enum Rating {
  BAD = 'BAD',
  NEUTRAL = 'NEUTRAL',
  GOOD = 'GOOD'
}

export interface IDrug {
  id?: number;
  name?: string;
  form?: Form;
  dose?: string;
  packaging?: string;
  price?: number;
  rating?: Rating;
  description?: string;
  substitute?: IDrug;
  producers?: IProducer[];
  diseases?: IDisease[];
}

export const defaultValue: Readonly<IDrug> = {};

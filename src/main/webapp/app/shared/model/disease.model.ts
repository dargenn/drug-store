import { IDrug } from 'app/shared/model//drug.model';

export interface IDisease {
  id?: number;
  name?: string;
  description?: string;
  drugs?: IDrug[];
}

export const defaultValue: Readonly<IDisease> = {};

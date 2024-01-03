import { Moment } from 'moment';

export interface IUser {
  _id: string;
  guid: string;
  name: {
    first: string;
    last: string;
  };
  age: number;
  index?: number;
  isActive?: boolean;
  balance?: string;
  picture?: string;
  eyeColor?: string;
  company?: string;
  email?: string;
  phone?: string;
  address?: string;
  about?: string;
  registered?: string;
  latitude?: string;
  longitude?: string;
  tags?: string[];
  fetchedAt?: Moment;
}

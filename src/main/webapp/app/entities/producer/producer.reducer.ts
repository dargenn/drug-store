import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProducer, defaultValue } from 'app/shared/model/producer.model';

export const ACTION_TYPES = {
  FETCH_PRODUCER_LIST: 'producer/FETCH_PRODUCER_LIST',
  FETCH_PRODUCER: 'producer/FETCH_PRODUCER',
  CREATE_PRODUCER: 'producer/CREATE_PRODUCER',
  UPDATE_PRODUCER: 'producer/UPDATE_PRODUCER',
  DELETE_PRODUCER: 'producer/DELETE_PRODUCER',
  RESET: 'producer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProducer>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ProducerState = Readonly<typeof initialState>;

// Reducer

export default (state: ProducerState = initialState, action): ProducerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCER):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCER):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCER):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCER):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCER):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCER_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCER):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/producers';

// Actions

export const getEntities: ICrudGetAllAction<IProducer> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCER_LIST,
    payload: axios.get<IProducer>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IProducer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCER,
    payload: axios.get<IProducer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProducer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IProducer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProducer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCER,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

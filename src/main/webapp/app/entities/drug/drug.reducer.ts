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

import { IDrug, defaultValue } from 'app/shared/model/drug.model';

export const ACTION_TYPES = {
  FETCH_DRUG_LIST: 'drug/FETCH_DRUG_LIST',
  FETCH_DRUG: 'drug/FETCH_DRUG',
  CREATE_DRUG: 'drug/CREATE_DRUG',
  UPDATE_DRUG: 'drug/UPDATE_DRUG',
  DELETE_DRUG: 'drug/DELETE_DRUG',
  RESET: 'drug/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDrug>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DrugState = Readonly<typeof initialState>;

// Reducer

export default (state: DrugState = initialState, action): DrugState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DRUG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DRUG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DRUG):
    case REQUEST(ACTION_TYPES.UPDATE_DRUG):
    case REQUEST(ACTION_TYPES.DELETE_DRUG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DRUG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DRUG):
    case FAILURE(ACTION_TYPES.CREATE_DRUG):
    case FAILURE(ACTION_TYPES.UPDATE_DRUG):
    case FAILURE(ACTION_TYPES.DELETE_DRUG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DRUG_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_DRUG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DRUG):
    case SUCCESS(ACTION_TYPES.UPDATE_DRUG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DRUG):
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

const apiUrl = 'api/drugs';

// Actions

export const getEntities: ICrudGetAllAction<IDrug> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DRUG_LIST,
    payload: axios.get<IDrug>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getFavouriteEntities: ICrudGetAllAction<IDrug> = (page, size, sort) => {
  const requestUrl = `api/favourite-drugs`;
  return {
    type: ACTION_TYPES.FETCH_DRUG_LIST,
    payload: axios.get<IDrug>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDrug> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DRUG,
    payload: axios.get<IDrug>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDrug> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DRUG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IDrug> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DRUG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDrug> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DRUG,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Drug from './drug';
import DrugDetail from './drug-detail';
import DrugUpdate from './drug-update';
import DrugDeleteDialog from './drug-delete-dialog';
import { FavouriteDrugs } from 'app/entities/drug/favourite-drugs';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`drugs/user/favourite-drugs`} component={FavouriteDrugs} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DrugUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DrugUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DrugDetail} />
      <ErrorBoundaryRoute path={match.url} component={Drug} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DrugDeleteDialog} />
  </>
);

export default Routes;

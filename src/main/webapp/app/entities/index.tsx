import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Drug from './drug';
import Producer from './producer';
import Disease from './disease';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/drug`} component={Drug} />
      <ErrorBoundaryRoute path={`${match.url}/producer`} component={Producer} />
      <ErrorBoundaryRoute path={`${match.url}/disease`} component={Disease} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;

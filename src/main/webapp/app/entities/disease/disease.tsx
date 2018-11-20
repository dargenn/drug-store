import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { getSession } from 'app/shared/reducers/authentication';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './disease.reducer';
import { IDisease } from 'app/shared/model/disease.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface IDiseaseProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Disease extends React.Component<IDiseaseProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { diseaseList, match, isSpecialist } = this.props;
    return (
      <div>
        {isSpecialist && (
          <h2 id="disease-heading">
            <Translate contentKey="drugstoreApp.disease.home.title">Diseases</Translate>
            <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="drugstoreApp.disease.home.createLabel">Create new Disease</Translate>
            </Link>
          </h2>
        )}
        <div className="table-responsive">
          <Table responsive striped>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="drugstoreApp.disease.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="drugstoreApp.disease.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {diseaseList.map((disease, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${disease.id}`} color="link" size="sm">
                      {disease.id}
                    </Button>
                  </td>
                  <td>{disease.name}</td>
                  <td>{disease.description}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${disease.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      {isSpecialist && (
                        <Button tag={Link} to={`${match.url}/${disease.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                      )}
                      {isSpecialist && (
                        <Button tag={Link} to={`${match.url}/${disease.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ disease, authentication }: IRootState) => ({
  diseaseList: disease.entities,
  isSpecialist: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.SPECIALIST])
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Disease);

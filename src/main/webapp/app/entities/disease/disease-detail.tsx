import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './disease.reducer';
import { IDisease } from 'app/shared/model/disease.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface IDiseaseDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DiseaseDetail extends React.Component<IDiseaseDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { diseaseEntity, isSpecialist } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="drugstoreApp.disease.detail.title">Disease</Translate> [<b>{diseaseEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="drugstoreApp.disease.name">Name</Translate>
              </span>
            </dt>
            <dd>{diseaseEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="drugstoreApp.disease.description">Description</Translate>
              </span>
            </dt>
            <dd>{diseaseEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/disease" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          {isSpecialist && (
            <Button tag={Link} to={`/entity/disease/${diseaseEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.edit">Edit</Translate>
              </span>
            </Button>
          )}
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ disease, authentication }: IRootState) => ({
  diseaseEntity: disease.entity,
  isSpecialist: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.SPECIALIST])
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiseaseDetail);

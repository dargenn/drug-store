import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './drug.reducer';
import { IDrug } from 'app/shared/model/drug.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface IDrugDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DrugDetail extends React.Component<IDrugDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { drugEntity, isSpecialist } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="drugstoreApp.drug.detail.title">Drug</Translate> [<b>{drugEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="drugstoreApp.drug.name">Name</Translate>
              </span>
            </dt>
            <dd>{drugEntity.name}</dd>
            <dt>
              <span id="form">
                <Translate contentKey="drugstoreApp.drug.form">Form</Translate>
              </span>
            </dt>
            <dd>{drugEntity.form}</dd>
            <dt>
              <span id="dose">
                <Translate contentKey="drugstoreApp.drug.dose">Dose</Translate>
              </span>
            </dt>
            <dd>{drugEntity.dose}</dd>
            <dt>
              <span id="packaging">
                <Translate contentKey="drugstoreApp.drug.packaging">Packaging</Translate>
              </span>
            </dt>
            <dd>{drugEntity.packaging}</dd>
            <dt>
              <span id="price">
                <Translate contentKey="drugstoreApp.drug.price">Price</Translate>
              </span>
            </dt>
            <dd>{drugEntity.price}</dd>
            <dt>
              <span id="rating">
                <Translate contentKey="drugstoreApp.drug.rating">Rating</Translate>
              </span>
            </dt>
            <dd>{drugEntity.rating}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="drugstoreApp.drug.description">Description</Translate>
              </span>
            </dt>
            <dd>{drugEntity.description}</dd>
            <dt>
              <Translate contentKey="drugstoreApp.drug.substitute">Substitute</Translate>
            </dt>
            <dd>{drugEntity.substitute ? drugEntity.substitute.name : ''}</dd>
            <dt>
              <Translate contentKey="drugstoreApp.drug.producer">Producer</Translate>
            </dt>
            <dd>
              {drugEntity.producers
                ? drugEntity.producers.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === drugEntity.producers.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
            <dt>
              <Translate contentKey="drugstoreApp.drug.diseases">Diseases</Translate>
            </dt>
            <dd>
              {drugEntity.diseases
                ? drugEntity.diseases.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.name}</a>
                      {i === drugEntity.diseases.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/drug" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          {isSpecialist && (
            <Button tag={Link} to={`/entity/drug/${drugEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ drug, authentication }: IRootState) => ({
  drugEntity: drug.entity,
  isSpecialist: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.SPECIALIST])
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DrugDetail);

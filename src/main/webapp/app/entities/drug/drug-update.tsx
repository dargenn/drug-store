import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getDrugs } from 'app/entities/drug/drug.reducer';
import { IProducer } from 'app/shared/model/producer.model';
import { getEntities as getProducers } from 'app/entities/producer/producer.reducer';
import { IDisease } from 'app/shared/model/disease.model';
import { getEntities as getDiseases } from 'app/entities/disease/disease.reducer';
import { getEntity, updateEntity, createEntity, reset } from './drug.reducer';
import { IDrug } from 'app/shared/model/drug.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDrugUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDrugUpdateState {
  isNew: boolean;
  idsproducer: any[];
  idsdiseases: any[];
  substituteId: string;
}

export class DrugUpdate extends React.Component<IDrugUpdateProps, IDrugUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsproducer: [],
      idsdiseases: [],
      substituteId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getDrugs();
    this.props.getProducers();
    this.props.getDiseases();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { drugEntity } = this.props;
      const entity = {
        ...drugEntity,
        ...values,
        producers: mapIdList(values.producers),
        diseases: mapIdList(values.diseases)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/drug');
  };

  render() {
    const { drugEntity, drugs, producers, diseases, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="drugstoreApp.drug.home.createOrEditLabel">
              <Translate contentKey="drugstoreApp.drug.home.createOrEditLabel">Create or edit a Drug</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : drugEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="drug-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="drugstoreApp.drug.name">Name</Translate>
                  </Label>
                  <AvField
                    id="drug-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="formLabel">
                    <Translate contentKey="drugstoreApp.drug.form">Form</Translate>
                  </Label>
                  <AvInput
                    id="drug-form"
                    type="select"
                    className="form-control"
                    name="form"
                    value={(!isNew && drugEntity.form) || 'TABLET'}
                  >
                    <option value="TABLET">
                      <Translate contentKey="drugstoreApp.Form.TABLET" />
                    </option>
                    <option value="SOLUTION">
                      <Translate contentKey="drugstoreApp.Form.SOLUTION" />
                    </option>
                    <option value="CAPSULE">
                      <Translate contentKey="drugstoreApp.Form.CAPSULE" />
                    </option>
                    <option value="PILL">
                      <Translate contentKey="drugstoreApp.Form.PILL" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="doseLabel" for="dose">
                    <Translate contentKey="drugstoreApp.drug.dose">Dose</Translate>
                  </Label>
                  <AvField
                    id="drug-dose"
                    type="text"
                    name="dose"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="packagingLabel" for="packaging">
                    <Translate contentKey="drugstoreApp.drug.packaging">Packaging</Translate>
                  </Label>
                  <AvField
                    id="drug-packaging"
                    type="text"
                    name="packaging"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="price">
                    <Translate contentKey="drugstoreApp.drug.price">Price</Translate>
                  </Label>
                  <AvField
                    id="drug-price"
                    type="text"
                    name="price"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="ratingLabel">
                    <Translate contentKey="drugstoreApp.drug.rating">Rating</Translate>
                  </Label>
                  <AvInput
                    id="drug-rating"
                    type="select"
                    className="form-control"
                    name="rating"
                    value={(!isNew && drugEntity.rating) || 'NEUTRAL'}
                  >
                    <option value="BAD">
                      <Translate contentKey="drugstoreApp.Rating.BAD" />
                    </option>
                    <option value="NEUTRAL">
                      <Translate contentKey="drugstoreApp.Rating.NEUTRAL" />
                    </option>
                    <option value="GOOD">
                      <Translate contentKey="drugstoreApp.Rating.GOOD" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="drugstoreApp.drug.description">Description</Translate>
                  </Label>
                  <AvField id="drug-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label for="substitute.name">
                    <Translate contentKey="drugstoreApp.drug.substitute">Substitute</Translate>
                  </Label>
                  <AvInput id="drug-substitute" type="select" className="form-control" name="substitute.id">
                    <option value="" key="0" />
                    {drugs
                      ? drugs.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="producers">
                    <Translate contentKey="drugstoreApp.drug.producer">Producer</Translate>
                  </Label>
                  <AvInput
                    id="drug-producer"
                    type="select"
                    multiple
                    className="form-control"
                    name="producers"
                    value={drugEntity.producers && drugEntity.producers.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {producers
                      ? producers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="diseases">
                    <Translate contentKey="drugstoreApp.drug.diseases">Diseases</Translate>
                  </Label>
                  <AvInput
                    id="drug-diseases"
                    type="select"
                    multiple
                    className="form-control"
                    name="diseases"
                    value={drugEntity.diseases && drugEntity.diseases.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {diseases
                      ? diseases.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/drug" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  drugs: storeState.drug.entities,
  producers: storeState.producer.entities,
  diseases: storeState.disease.entities,
  drugEntity: storeState.drug.entity,
  loading: storeState.drug.loading,
  updating: storeState.drug.updating,
  updateSuccess: storeState.drug.updateSuccess
});

const mapDispatchToProps = {
  getDrugs,
  getProducers,
  getDiseases,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DrugUpdate);

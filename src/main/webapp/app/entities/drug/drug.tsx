import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table, Badge } from 'reactstrap';
import { getSession } from 'app/shared/reducers/authentication';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './drug.reducer';
import { IDrug } from 'app/shared/model/drug.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface IDrugProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IDrugState = IPaginationBaseState;

export class Drug extends React.Component<IDrugProps, IDrugState> {
  state: IDrugState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { drugList, match, isSpecialist } = this.props;
    return (
      <div>
        <h2 id="drug-heading">
          <Translate contentKey="drugstoreApp.drug.home.title">Drugs</Translate>
        </h2>
        <div className="pull-right">
          {isSpecialist && (
            <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="drugstoreApp.drug.home.createLabel">Create new Drug</Translate>
            </Link>
          )}
        </div>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            <Table responsive striped>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('name')}>
                    <Translate contentKey="drugstoreApp.drug.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('form')}>
                    <Translate contentKey="drugstoreApp.drug.form">Form</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('dose')}>
                    <Translate contentKey="drugstoreApp.drug.dose">Dose</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('packaging')}>
                    <Translate contentKey="drugstoreApp.drug.packaging">Packaging</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('price')}>
                    <Translate contentKey="drugstoreApp.drug.price">Price</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('rating')}>
                    <Translate contentKey="drugstoreApp.drug.rating">Rating</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('description')}>
                    <Translate contentKey="drugstoreApp.drug.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="drugstoreApp.drug.substitute">Substitute</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>Producers</th>
                  <th>Diseases</th>
                  <th />
                  <th />
                </tr>
              </thead>
              <tbody>
                {drugList.map((drug, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${drug.id}`} color="link" size="sm">
                        {drug.id}
                      </Button>
                    </td>
                    <td>{drug.name}</td>
                    <td>
                      <Translate contentKey={`drugstoreApp.Form.${drug.form}`} />
                    </td>
                    <td>{drug.dose}</td>
                    <td>{drug.packaging}</td>
                    <td>{drug.price}</td>
                    <td>
                      <Translate contentKey={`drugstoreApp.Rating.${drug.rating}`} />
                    </td>
                    <td>{drug.description}</td>
                    <td>{drug.substitute ? <Link to={`drug/${drug.substitute.id}`}>{drug.substitute.name}</Link> : ''}</td>
                    <td>
                      {drug.producers
                        ? drug.producers.map((authority, j) => (
                            <div key={`user-auth-${i}-${j}`}>
                              <Badge color="info">{authority.name}</Badge>
                            </div>
                          ))
                        : null}
                    </td>
                    <td>
                      {drug.diseases
                        ? drug.diseases.map((authority, j) => (
                            <div key={`user-auth-${i}-${j}`}>
                              <Badge color="info">{authority.name}</Badge>
                            </div>
                          ))
                        : null}
                    </td>
                    <td>
                      <FontAwesomeIcon color="orange" icon="star" />{' '}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${drug.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        {isSpecialist && (
                          <Button tag={Link} to={`${match.url}/${drug.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.edit">Edit</Translate>
                            </span>
                          </Button>
                        )}
                        {isSpecialist && (
                          <Button tag={Link} to={`${match.url}/${drug.id}/delete`} color="danger" size="sm">
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
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ authentication, drug }: IRootState) => ({
  drugList: drug.entities,
  totalItems: drug.totalItems,
  links: drug.links,
  entity: drug.entity,
  updateSuccess: drug.updateSuccess,
  isSpecialist: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.SPECIALIST])
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Drug);

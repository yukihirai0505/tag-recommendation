import React, {Component} from 'react';
import {getTagInfo} from '../../utils/request-api'
import {TextBox} from '../Components/TextBox'

class Home extends Component {
  constructor() {
    super();
    this.state = {tags: [], form: {}}
  }

  componentWillMount() {

  }

  onChange = (e) => {
    this.state.form[e.target.name] = e.target.value;
    this.setState({form: this.state.form});
  };

  handleSubmit = (e) => {
    e.preventDefault();
    const {form} = this.state;
    const tagName = form.name
    getTagInfo(tagName).then(res => {
      console.info(res);
      this.setState({tags: res})
    });

  };

  render() {
    const {tags, form} = this.state;

    return (
      <div className="animated fadeIn">
        <div className="row">
          <div className="col-md-12">
            <div className="card ">
              <div className="card-header">
                タグ検索
              </div>
              <form
                className="form-horizontal"
                onSubmit={this.handleSubmit}
              >
                <div className="card-block">
                  <TextBox name='name'
                           labelName='タグ'
                           placeholder="タグ名を入れてください"
                           onChange={this.onChange}
                  />
                </div>
                <div className="card-footer text-right">
                  <button type="submit" className="btn btn-sm btn-primary "><i className="fa fa-dot-circle-o"/> 検索
                  </button>
                </div>
              </form>
            </div>
          </div>
          <div className="col-md-12">
            <div className="card">
              <div className="card-header">
                <i className="fa fa-align-justify"/> おすすめタグ一覧一覧
              </div>
              <div className="card-block">
                <table className="table">
                  <thead>
                  <tr>
                    <th className="col-sm-3 col-xs-4">タグ</th>
                  </tr>
                  </thead>
                  <tbody>
                  {renderTags(tags)}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

function renderTags(tags = []) {
  return tags.map((recommendInfo, key) => {
    return (
      <tr key={key}>
        <td>
          {recommendInfo}
        </td>
      </tr>)
  })
}

export default Home;

import React, {Component} from 'react';

class TextBox extends Component {
  render() {
    return (
      <div className="form-group">
        <label htmlFor={this.props.name}>{this.props.labelName}</label>
        <input type="text"
               className="form-control"
               id={this.props.name}
               name={this.props.name}
               placeholder={this.props.placeholder}
               onChange={this.props.onChange}/>
      </div>
    );
  }
}

export {TextBox}
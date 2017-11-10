import axios from 'axios';
import { CONFIG_MULTIPART_FORM_DATA } from '../helper/helperAxios';

const BASE_URL = 'https://api.yabaiwebyasan.com/v1';

export {getRecommendTags, getTagInfo};

function getRecommendTags(tagName) {
  const url = `${BASE_URL}/instagram/tags/recommend/${tagName}`;
  return axios.get(url).then(response => response.data).catch(response => {
    console.log(response)
  });
}

function getTagInfo(tagName) {
  const url = `${BASE_URL}/instagram/tags/${tagName}/media`;
  return axios.get(url).then(response => response.data).catch(response => {
    console.log(response)
  });
}
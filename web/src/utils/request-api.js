import axios from 'axios';
import { CONFIG_MULTIPART_FORM_DATA } from '../helper/helperAxios';

const BASE_URL = 'https://api.yabaiwebyasan.com/v1';

export {getTagInfo};

function getTagInfo(tagName) {
  const url = `${BASE_URL}/instagram/tags/recommend/${tagName}`;
  return axios.get(url).then(response => response.data).catch(response => {
    console.log(response)
  });
}


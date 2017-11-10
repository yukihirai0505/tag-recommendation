export function getPathName(pathName) {
  const paths = [
    {path: '/', name: 'ホーム'},
    {path: '/recommend', name: 'レコメンド'}
  ]
  return paths.map((p) => {
    if (p.path === pathName) {
      return p.name
    }
  })
}

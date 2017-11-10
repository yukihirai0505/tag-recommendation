export function getLabel(obj, value) {
  let label
  obj.forEach((item) => {
    if (item.value === value) return label = item.label
  })
  return label
}
exports.lambdaHandler = async (event) => {
    console.log('--- Events ---')
    console.log(JSON.stringify(event, null, 2))
}
def call(body) {

  // evaluate the body block, and collect configuration into the object

  def config = [:]
  print config
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  // now build, based on the configuration provided
  node {

    print "${body.delegate}"
    print "${body.delegate.name}"

  }
}

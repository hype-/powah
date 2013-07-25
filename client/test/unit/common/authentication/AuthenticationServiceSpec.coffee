describe 'authentication service', ->
  authenticationService = undefined

  beforeEach(module('services.AuthenticationService'))
  beforeEach(inject ($injector) ->
    authenticationService = $injector.get('authenticationService')
  )

  it 'exists', ->
    expect(authenticationService).not.toBeUndefined()

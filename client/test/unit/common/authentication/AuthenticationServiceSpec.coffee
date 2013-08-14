describe 'authentication service', ->
  authenticationService = undefined

  beforeEach(module('powah'))
  beforeEach(inject ($injector) ->
    authenticationService = $injector.get('AuthenticationService')
  )

  it 'exists', ->
    expect(authenticationService).not.toBeUndefined()

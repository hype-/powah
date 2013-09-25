describe 'authentication service', ->
  $httpBackend = undefined

  beforeEach(module('powah'))

  afterEach ->
    $httpBackend.verifyNoOutstandingExpectation()
    $httpBackend.verifyNoOutstandingRequest()

  it 'gets current user if not already in session and sets it to session', ->
    authenticationService = undefined
    sessionService = undefined

    cookieParser = {
      get: jasmine.createSpy().andReturn("foo@bar.com")
    }

    module ($provide) ->
      $provide.value('CookieParser', cookieParser)
      null

    inject ($injector) ->
      $httpBackend = $injector.get('$httpBackend')
      sessionService = $injector.get('SessionService')
      authenticationService = $injector.get('AuthenticationService')

    authenticationService.tryToAuthenticateWithCurrentUser()

    expect(sessionService.getUser().username).toBe("foo@bar.com")

  it 'does not set user to session when not authenticated', ->
    authenticationService = undefined
    sessionService = undefined

    cookieParser = {
      get: jasmine.createSpy().andReturn(undefined)
    }

    module ($provide) ->
      $provide.value('CookieParser', cookieParser)
      null

    inject ($injector) ->
      $httpBackend = $injector.get('$httpBackend')
      sessionService = $injector.get('SessionService')
      authenticationService = $injector.get('AuthenticationService')

    authenticationService.tryToAuthenticateWithCurrentUser()

    expect(sessionService.getUser().username).toBe(null)

  it 'does not try to get current user if already in session', ->
    authenticationService = undefined
    sessionService = undefined

    cookieParser = {
      get: jasmine.createSpy().andReturn(undefined)
    }

    module ($provide) ->
      $provide.value('CookieParser', cookieParser)
      null

    inject ($injector) ->
      $httpBackend = $injector.get('$httpBackend')
      sessionService = $injector.get('SessionService')
      authenticationService = $injector.get('AuthenticationService')

    sessionService.setUsername('foo')

    authenticationService.tryToAuthenticateWithCurrentUser()

    expect(sessionService.getUser().username).toBe('foo')

  it 'is logged in when SessionService has user', ->
    authenticationService = undefined
    sessionService = undefined

    inject ($injector) ->
      $httpBackend = $injector.get('$httpBackend')
      sessionService = $injector.get('SessionService')
      authenticationService = $injector.get('AuthenticationService')

    expect(authenticationService.isLoggedIn()).toBe(false)

    sessionService.setUsername('foo')

    expect(authenticationService.isLoggedIn()).toBe(true)

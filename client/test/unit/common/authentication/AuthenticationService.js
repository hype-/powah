describe('authentication service', function () {
  var $httpBackend;

  beforeEach(module('powah'));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('gets current user if not already in session and sets it to session', function () {
    var authenticationService, sessionService;

    var cookieParser = {
      get: jasmine.createSpy().andReturn("foo@bar.com")
    };

    module(function ($provide) {
      $provide.value('CookieParser', cookieParser);
    });

    inject(function ($injector) {
      $httpBackend = $injector.get('$httpBackend');
      sessionService = $injector.get('SessionService');
      authenticationService = $injector.get('AuthenticationService');
    });

    authenticationService.tryToAuthenticateWithCurrentUser();

    expect(sessionService.getUser().username).toBe("foo@bar.com");
  });

  it('does not set user to session when not authenticated', function () {
    var authenticationService, sessionService;

    var cookieParser = {
      get: jasmine.createSpy().andReturn(void 0)
    };

    module(function ($provide) {
      $provide.value('CookieParser', cookieParser);
    });

    inject(function ($injector) {
      $httpBackend = $injector.get('$httpBackend');
      sessionService = $injector.get('SessionService');
      authenticationService = $injector.get('AuthenticationService');
    });

    authenticationService.tryToAuthenticateWithCurrentUser();

    expect(sessionService.getUser().username).toBe(null);
  });

  it('does not try to get current user if already in session', function () {
    var authenticationService, sessionService;

    var cookieParser = {
      get: jasmine.createSpy().andReturn(void 0)
    };

    module(function ($provide) {
      $provide.value('CookieParser', cookieParser);
    });

    inject(function ($injector) {
      $httpBackend = $injector.get('$httpBackend');
      sessionService = $injector.get('SessionService');
      authenticationService = $injector.get('AuthenticationService');
    });

    sessionService.setUsername('foo');

    authenticationService.tryToAuthenticateWithCurrentUser();

    expect(sessionService.getUser().username).toBe('foo');
  });

  it('is logged in when SessionService has user', function () {
    var authenticationService, sessionService;

    inject(function ($injector) {
      $httpBackend = $injector.get('$httpBackend');
      sessionService = $injector.get('SessionService');
      authenticationService = $injector.get('AuthenticationService');
    });

    expect(authenticationService.isLoggedIn()).toBe(false);

    sessionService.setUsername('foo');

    expect(authenticationService.isLoggedIn()).toBe(true);
  });
});

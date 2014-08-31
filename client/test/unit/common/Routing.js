describe('routing', function () {
  var $route;

  beforeEach(module('powah'));
  beforeEach(inject(function ($injector) {
    $route = $injector.get('$route');
  }));

  it('defines /home', function () {
    var home = $route.routes['/home'];

    expect(home.templateUrl).toBe('home/home.tpl.html');
    expect(home.controller).toBe('HomeCtrl');
  });
});

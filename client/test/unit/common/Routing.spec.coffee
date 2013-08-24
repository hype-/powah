describe 'routing', ->
  $route = undefined

  beforeEach(module('powah'))
  beforeEach(inject ($injector) ->
    $route = $injector.get('$route')
  )

  it 'defines /home', ->
    home = $route.routes['/home']

    expect(home.templateUrl).toBe('home/home.tpl.html')
    expect(home.controller).toBe('HomeCtrl')

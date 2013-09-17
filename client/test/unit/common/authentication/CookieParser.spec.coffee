describe 'cookie parser', ->
  $cookies = undefined
  cookieParser = undefined

  beforeEach(module('powah'))
  beforeEach(
    inject ($injector) ->
      $cookies = $injector.get('$cookies')
      cookieParser = $injector.get('CookieParser')

      # Actual cookie:
      # PLAY_SESSION="89b19045c0264cce7a2290ef9df7353b290d4e52-username=foo&luss=hoff&xoo=2"; Path=/
      $cookies['PLAY_SESSION'] = '"89b19045c0264cce7a2290ef9df7353b290d4e52-username=foo&luss=hoff&xoo=2"'
  )

  it 'parses and gets variables from cookie', ->
    expect(cookieParser.get('username')).toBe('foo')
    expect(cookieParser.get('luss')).toBe('hoff')
    expect(cookieParser.get('xoo')).toBe('2')

  it 'returns undefined for not found variable', ->
    expect(cookieParser.get('notfound')).not.toBeDefined()

  it 'returns undefined without cookie', ->
    delete $cookies['PLAY_SESSION']

    expect(cookieParser.get('username')).not.toBeDefined()

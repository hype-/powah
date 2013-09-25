describe 'authentication', ->
  beforeEach ->
    browser().navigateTo('/')

  it 'displays Login on the front page', ->
    expect(element('h1').text()).toBe('Login')

  it 'logins and displays welcome message', ->
    input('credentials.username').enter('lussen')
    input('credentials.password').enter('hoff')
    element(':submit').click()

    expect(element('h2').text()).toBe('Welcome, lussen')

  it 'redirects to front page when trying to access an url anonymously', ->
    browser().navigateTo('/#/home')

    expect(browser().location().path()).toBe('/')

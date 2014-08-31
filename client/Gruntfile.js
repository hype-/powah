module.exports = function (grunt) {
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-recess');
  grunt.loadNpmTasks('grunt-karma');
  grunt.loadNpmTasks('grunt-html2js');

  // Default task.
  grunt.registerTask('default', ['jshint', 'build', 'karma:unit', 'karma:integration']);
  grunt.registerTask('build', ['clean', 'html2js', 'concat', 'recess:build', 'copy:assets']);
  grunt.registerTask('release', ['clean', 'html2js', 'uglify', 'jshint', 'karma:unit', 'karma:integration', 'concat:index', 'recess:min', 'copy:assets']);
  grunt.registerTask('unit-test-watch', ['karma:unitWatch']);
  grunt.registerTask('integration-test-watch', ['karma:integrationWatch']);

  // Print a timestamp (useful for when watching)
  grunt.registerTask('timestamp', function() {
    grunt.log.subhead(new Date());
  });

  var karmaConfig = function(configFile, customOptions) {
    var options = { configFile: configFile, keepalive: true };
    var travisOptions = process.env.TRAVIS && { browsers: ['Firefox'], reporters: 'dots' };
    return grunt.util._.extend(options, customOptions, travisOptions);
  };

  // Project configuration.
  grunt.initConfig({
    distdir: 'dist',
    pkg: grunt.file.readJSON('package.json'),
    banner:
      '/*! <%= pkg.name %> - <%= grunt.template.today("yyyy-mm-dd") %> - ' +
      'Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author %> */\n',
    src: {
      gruntfile: ['Gruntfile.js'],
      js: ['src/**/*.js'],
      jsTest: ['test/unit/**/*.js', 'test/integration/**/*.js'],
      html: ['src/index.html', 'test/integration/index.html'],
      distTpl: ['<%= distdir %>/templates/**/*.js'],
      tpl: {
        app: ['src/app/**/*.tpl.html'],
        common: ['src/common/**/*.tpl.html']
      },
      less: ['src/less/stylesheet.less'], // recess:build doesn't accept ** in its file patterns
      lessWatch: ['src/less/**/*.less']
    },
    clean: ['<%= distdir %>/*'],
    copy: {
      assets: {
        files: [{ dest: '<%= distdir %>', src : '**', expand: true, cwd: 'src/assets/' }]
      }
    },
    karma: {
      unit: { options: karmaConfig('test/config/unit.js') },
      integration: { options: karmaConfig('test/config/integration.js') },
      unitWatch: { options: karmaConfig('test/config/unit.js', { singleRun: false, autoWatch: true }) },
      integrationWatch: { options: karmaConfig('test/config/integration.js', { singleRun: false, autoWatch: true }) }
    },
    html2js: {
      app: {
        options: {
          base: 'src/app'
        },
        src: ['<%= src.tpl.app %>'],
        dest: '<%= distdir %>/templates/app.js',
        module: 'templates.app'
      },
      common: {
        options: {
          base: 'src/common'
        },
        src: ['<%= src.tpl.common %>'],
        dest: '<%= distdir %>/templates/common.js',
        module: 'templates.common'
      }
    },
    concat: {
      dist: {
        options: {
          banner: "<%= banner %>"
        },
        src: ['<%= src.js %>', '<%= src.distTpl %>'],
        dest: '<%= distdir %>/<%= pkg.name %>.js'
      },
      index: {
        src: ['src/index.html'],
        dest: '<%= distdir %>/index.html',
        options: {
          process: true
        }
      },
      vendor: {
        src: ['vendor/angular/angular.js', 'vendor/angular/angular-cookies.js', 'vendor/underscore/underscore.js'],
        dest: '<%= distdir %>/vendor.js'
      },
      testApp: {
        src: ['test/integration/app.js'],
        dest: '<%= distdir %>/test-powah.js'
      },
      testIndex: {
        src: ['test/integration/index.html'],
        dest: '<%= distdir %>/test-index.html',
        options: {
          process: true
        }
      },
      testAngularMocks: {
        src: ['test/vendor/angular/angular-mocks.js'],
        dest: '<%= distdir %>/angular-mocks.js'
      }
    },
    uglify: {
      dist: {
        options: {
          banner: "<%= banner %>"
        },
        src: ['<%= src.js %>', '<%= src.distTpl %>'],
        dest: '<%= distdir %>/<%= pkg.name %>.js'
      },
      vendor: {
        src: ['<%= concat.vendor.src %>'],
        dest: '<%= distdir %>/vendor.js'
      }
    },
    recess: {
      build: {
        files: {
          '<%= distdir %>/<%= pkg.name %>.css':
          ['<%= src.less %>']
        },
        options: {
          compile: true
        }
      },
      min: {
        files: {
          '<%= distdir %>/<%= pkg.name %>.css': ['<%= src.less %>']
        },
        options: {
          compress: true
        }
      }
    },
    watch: {
      all: {
        files: ['<%= src.gruntfile %>', '<%= src.js %>', '<%= src.jsTest %>', '<%= src.lessWatch %>', '<%= src.tpl.app %>', '<%= src.tpl.common %>', '<%= src.html %>'],
        tasks: ['default', 'timestamp']
      },
      build: {
        files: ['<%= src.gruntfile %>', '<%= src.js %>', '<%= src.jsTest %>', '<%= src.lessWatch %>', '<%= src.tpl.app %>', '<%= src.tpl.common %>', '<%= src.html %>'],
        tasks: ['build', 'timestamp']
      }
    },
    jshint: {
      files: ['<%= src.gruntfile %>', '<%= src.js %>', '<%= src.jsTest %>'],
      options: {
        curly: true,
        eqeqeq: true,
        immed: true,
        latedef: true,
        newcap: true,
        noarg: true,
        sub: true,
        boss: true,
        eqnull: true,
        globals: {}
      }
    }
  });
};

grunt.initConfig({
  mavenPrepare: {
    options: {
      resources: ['**']
    },
    prepare: {}
  },

  mavenDist: {
    options: {
      warName: '<%= gruntMavenProperties.warName %>',
      deliverables: ['**', '!non-deliverable.js'],
      gruntDistDir: 'dist'
    },
    dist: {}
  },

  gruntMavenProperties: grunt.file.readJSON('grunt-maven.json'),

  watch: {
    maven: {
      files: ['<%= gruntMavenProperties.filesToWatch %>'],
      tasks: 'default'
    }
  }
});

grunt.loadNpmTasks('grunt-maven');

grunt.registerTask('default', ['mavenPrepare', 'jshint', 'karma', 'less', 'uglify', 'mavenDist']);

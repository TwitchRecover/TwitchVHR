package twitchvhr

import (
	"net/http"
	"strconv"
	"sync"
	"time"
)

type Options struct {
	Timeout          int
	AllowSource      bool
	Player           string
	AllowSpectre     bool
	AllowAudioOnly   bool
	IncludeFramerate bool
}

func Retrieve(vodIDs []int, options Options) []string {
	client := http.Client{Timeout: time.Duration(options.Timeout) * time.Second}
	feedsOptions := FeedsOption{
		AllowSource:      options.AllowSource,
		Player:           options.Player,
		AllowSpectre:     options.AllowSpectre,
		AllowAudioOnly:   options.AllowAudioOnly,
		IncludeFramerate: options.IncludeFramerate,
	}
	newHosts := make(chan []string)
	hosts := make(chan []string)
	var wg sync.WaitGroup
	defer close(hosts)
	go CombineResults(newHosts, hosts, &wg)
	for _, vod := range vodIDs {
		wg.Add(1)
		go RetrieveHosts(strconv.Itoa(vod), client, feedsOptions, newHosts, &wg)
	}
	wg.Wait()
	close(newHosts)
	results := <-hosts
	return UniqueResults(results)
}

func CombineResults(newHosts chan []string, hosts chan []string, wg *sync.WaitGroup) {
	results := make([]string, 0)
	for host := range newHosts {
		results = append(results, host...)
	}
	hosts <- results
}

func UniqueResults(results []string) []string {
	occured := map[string]bool{}
	uniqueResults := make([]string, 0)
	for r := range results {
		if occured[results[r]] != true {
			occured[results[r]] = true
			uniqueResults = append(uniqueResults, results[r])
		}
	}
	return uniqueResults
}

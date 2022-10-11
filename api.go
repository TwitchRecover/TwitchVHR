package twitchvhr

import (
	"bufio"
	"io"
	"net/http"
	"strconv"
	"strings"

	"github.com/TwitchRecover/TwitchGQL"
)

type Token struct {
	Signature string
	Token     string
}

type FeedsOption struct {
	AllowSource      bool
	Player           string
	AllowSpectre     bool
	AllowAudioOnly   bool
	IncludeFramerate bool
}

func FetchFeeds(vodID string, client http.Client, options FeedsOption) []string {
	feedUrls := make([]string, 0)
	token := RetrieveToken(vodID, client)
	feedOptions := "&allow_source=" + strconv.FormatBool(options.AllowSource) + "&player=" + options.Player + "&allow_spectre=" + strconv.FormatBool(options.AllowSpectre) + "&allow_audio_only=" + strconv.FormatBool(options.AllowAudioOnly) + "&playlist_include_framerate:" + strconv.FormatBool(options.IncludeFramerate)
	feedRes, _ := client.Get("https://usher.ttvnw.net/vod/" + vodID + ".m3u8?sig=" + token.Signature + "&token=" + token.Token + feedOptions)
	if feedRes.StatusCode != 200 {
		return feedUrls
	}
	defer feedRes.Body.Close()
	feeds, _ := io.ReadAll(feedRes.Body)
	scanner := bufio.NewScanner(strings.NewReader(string(feeds)))
	for scanner.Scan() {
		feed := scanner.Text()
		if !strings.HasPrefix(feed, "#") {
			feedUrls = append(feedUrls, feed)
		}
	}
	return feedUrls
}

func RetrieveToken(vodID string, httpClient http.Client) Token {
	id, _ := strconv.Atoi(vodID)
	client := twitchgql.Client{
		Client: httpClient,
	}
	video := &twitchgql.Video{}
	video.Request = twitchgql.VideoRequest{
		Params:              twitchgql.VideoRequestParams{Id: id},
		PlaybackAccessToken: &twitchgql.PlaybackAccessToken{Request: twitchgql.PlaybackAccessTokenRequest{Signature: true, Value: true}},
	}
	twitchgql.Query(client, video)
	return Token{
		Signature: video.Response.PlaybackAccessToken.Response.Signature,
		Token:     video.Response.PlaybackAccessToken.Response.Value,
	}
}
